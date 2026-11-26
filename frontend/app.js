const $ = id => document.getElementById(id);
let items = {};

function toast(msg, ok = true) {
  const t = $('toast');
  t.textContent = msg;
  t.style.background = ok ? 'var(--ok)' : 'var(--err)';
  t.style.display = 'block';
  clearTimeout(toast.timer);
  toast.timer = setTimeout(() => t.style.display = 'none', 3500);
}

async function api(path, method = 'GET', body) {
  const r = await fetch('/api' + path, {
    method,
    headers: {'Content-Type': 'application/json'},
    body: body ? JSON.stringify(body) : undefined
  });
  if (!r.ok) throw new Error('Ошибка ' + r.status);
  const text = await r.text();
  return text ? JSON.parse(text) : null;
}

async function act(fn) {
  try { await fn(); } catch (e) { toast(e.message, false); }
}

const me = () => localStorage.getItem('userId');

async function showMe() {
  if (!me()) { $('meInfo').textContent = 'не выбран'; return; }
  try {
    const u = await api('/users/' + me());
    $('meInfo').textContent = `${u.username} (id ${u.id}), баланс: ${u.balance}`;
  } catch (e) {
    $('meInfo').textContent = 'игрок не найден';
  }
}

async function selectUser() {
  localStorage.setItem('userId', $('uid').value);
  await showMe();
}

async function createUser() {
  const u = await api('/users', 'POST', {username: $('uname').value, balance: Number($('ubal').value || 0)});
  localStorage.setItem('userId', u.id);
  $('uid').value = u.id;
  toast('Игрок создан, id ' + u.id);
  await showMe();
}

async function loadItems() {
  const list = await api('/market/items');
  const sel = $('itemSel');
  items = {};
  sel.replaceChildren();
  list.forEach(i => {
    items[i.id] = i;
    const o = document.createElement('option');
    o.value = i.id;
    o.textContent = `${i.title} (${i.rarity})`;
    sel.appendChild(o);
  });
}

async function createItem() {
  await api('/market/items', 'POST', {title: $('iname').value, rarity: $('irar').value});
  toast('Предмет создан');
  await loadItems();
  await loadLots();
}

async function createLot() {
  if (!me()) return toast('Сначала выбери игрока', false);
  if (!$('itemSel').value) return toast('Сначала создай предмет', false);
  await api('/market/lots', 'POST', {
    itemId: Number($('itemSel').value),
    sellerId: Number(me()),
    price: Number($('price').value)
  });
  toast('Лот выставлен');
  await loadLots();
}

async function loadLots() {
  const lots = await api('/market/lots');
  const tb = $('lots');
  tb.replaceChildren();
  lots.forEach(l => {
    const it = items[l.itemId];
    const tr = document.createElement('tr');
    [l.id, it ? it.title : 'Предмет #' + l.itemId, it ? it.rarity : '-', l.price, 'игрок ' + l.sellerId]
      .forEach(v => { const td = document.createElement('td'); td.textContent = v; tr.appendChild(td); });
    const td = document.createElement('td');
    const b = document.createElement('button');
    b.className = 'small';
    b.textContent = 'Купить';
    b.onclick = () => act(() => buy(l.id));
    td.appendChild(b);
    tr.appendChild(td);
    tb.appendChild(tr);
  });
  if (!lots.length) {
    const tr = document.createElement('tr');
    const td = document.createElement('td');
    td.colSpan = 6;
    td.textContent = 'Активных лотов нет';
    tr.appendChild(td);
    tb.appendChild(tr);
  }
}

async function buy(lotId) {
  if (!me()) return toast('Сначала выбери игрока', false);
  try {
    await api(`/market/lots/${lotId}/buy`, 'POST', {buyerId: Number(me())});
    toast('Покупка выполнена');
  } catch (e) {
    toast('Не удалось купить (не хватает золота или лот уже продан)', false);
  }
  await showMe();
  await loadLots();
}

$('uid').value = me() || '';
act(async () => { await loadItems(); await loadLots(); await showMe(); });

$('btnSelect').addEventListener('click', () => act(selectUser));
$('btnCreateUser').addEventListener('click', () => act(createUser));
$('btnCreateItem').addEventListener('click', () => act(createItem));
$('btnCreateLot').addEventListener('click', () => act(createLot));
$('btnRefresh').addEventListener('click', () => act(loadLots));