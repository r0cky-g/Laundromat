const machineStatuses = new Map([
  ['AVAIL', ['Available', 'success']],
  ['RES', ['Reserved', 'warning']],
  ['IN_USE', ['In use', 'info']],
  ['MAINTENANCE', ['Maintenance', 'warning']],
  ['OUT_OF_SERVICE', ['Out of service', 'error']],
]);

const machineGroups = [
  { type: 'W', label: 'Washer', listId: 'washers' },
  { type: 'D', label: 'Dryer', listId: 'dryers' },
];

const machineCard = document.getElementById('machine-card');
const machineFeed = document.getElementById('machine-feed');
const refreshMachines = document.getElementById('refresh-machines');

const demoMachines = machineGroups.flatMap(({ type }, groupIndex) =>
  ['AVAIL', 'IN_USE', 'AVAIL', 'RES', 'AVAIL', groupIndex ? 'MAINTENANCE' : 'OUT_OF_SERVICE']
    .map((status, index) => ({ machineType: type, machineId: index + 1, status }))
);

function renderMachineList(machines) {
  for (const { type, label, listId } of machineGroups) {
    const group = machines.filter(machine => machine.machineType === type)
      .sort((left, right) => left.machineId - right.machineId);
    const list = document.getElementById(listId);
    list.replaceChildren(...group.map(machine => {
      const card = machineCard.content.firstElementChild.cloneNode(true);
      const [status, variant] = machineStatuses.get(machine.status) ?? ['Unknown', 'default'];
      card.querySelector('h3').textContent = `${label} ${String(machine.machineId).padStart(2, '0')}`;
      const badge = card.querySelector('.badge');
      badge.textContent = status;
      badge.classList.add(`badge--${variant}`);
      if (type === 'D') card.querySelector('.machine-icon').classList.add('machine-icon--dryer');
      return card;
    }));
    document.getElementById(`${listId}-empty`).hidden = group.length > 0;
  }
}

async function loadMachineStatuses() {
  refreshMachines.disabled = true;
  machineFeed.textContent = 'Loading machine statuses…';
  try {
    const machines = await api.get('/api/machines', { signal: AbortSignal.timeout(3000) });
    if (!Array.isArray(machines)) throw new Error('Expected a machine list');
    renderMachineList(machines);
    machineFeed.textContent = 'Showing statuses from the machine service.';
  } catch {
    renderMachineList(demoMachines);
    machineFeed.textContent = '';
  } finally {
    refreshMachines.disabled = false;
  }
}

refreshMachines.addEventListener('click', loadMachineStatuses);
loadMachineStatuses();
