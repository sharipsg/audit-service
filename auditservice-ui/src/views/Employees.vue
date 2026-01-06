<template>
  <div class="container">
    <div class="header">
      <div class="header-left">
        <h1>👥 Employees</h1>
      </div>
      <div class="header-right">
        <span class="user-badge">{{ userEmail }}</span>
        <button @click="logout" class="btn btn-secondary btn-small">Logout</button>
      </div>
    </div>

    <div v-if="error" class="alert alert-error">
      {{ error }}
    </div>

    <div v-if="success" class="alert alert-success">
      {{ success }}
    </div>

    <div class="actions-bar">
      <button @click="showCreateModal = true" class="btn btn-primary">
        + Create Employee
      </button>
    </div>

    <div class="card">
      <div v-if="loading" class="empty-state">
        <p>Loading employees...</p>
      </div>

      <div v-else-if="employees.length === 0" class="empty-state">
        <p>No employees found</p>
        <p style="font-size: 0.9rem; margin-top: 0.5rem;">Click "Create Employee" to add one</p>
      </div>

      <table v-else class="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Location</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="employee in employees" :key="employee.id">
            <td>{{ employee.id }}</td>
            <td>{{ employee.name }}</td>
            <td>{{ employee.email }}</td>
            <td>{{ employee.location }}</td>
            <td>
              <div class="actions">
                <button @click="editEmployee(employee)" class="btn btn-edit btn-small">
                  Edit
                </button>
                <button @click="viewAudit(employee)" class="btn btn-view btn-small">
                  View Audit
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Create Modal -->
    <div v-if="showCreateModal" class="modal-overlay" @click.self="showCreateModal = false">
      <div class="modal">
        <h2>Create Employee</h2>
        <form @submit.prevent="createEmployee">
          <div class="form-group">
            <label>Name</label>
            <input v-model="newEmployee.name" class="input" placeholder="John Doe" required />
          </div>
          <div class="form-group">
            <label>Email</label>
            <input v-model="newEmployee.email" type="email" class="input" placeholder="john@example.com" required />
          </div>
          <div class="form-group">
            <label>Location</label>
            <input v-model="newEmployee.location" class="input" placeholder="New York" required />
          </div>
          <div class="modal-actions">
            <button type="button" @click="showCreateModal = false" class="btn btn-secondary">Cancel</button>
            <button type="submit" class="btn btn-primary" :disabled="saving">
              {{ saving ? 'Creating...' : 'Create' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Edit Modal -->
    <div v-if="showEditModal" class="modal-overlay" @click.self="showEditModal = false">
      <div class="modal">
        <h2>Edit Employee</h2>
        <form @submit.prevent="updateEmployee">
          <div class="form-group">
            <label>Name</label>
            <input v-model="editingEmployee.name" class="input" />
          </div>
          <div class="form-group">
            <label>Email</label>
            <input v-model="editingEmployee.email" type="email" class="input" />
          </div>
          <div class="form-group">
            <label>Location</label>
            <input v-model="editingEmployee.location" class="input" />
          </div>
          <div class="modal-actions">
            <button type="button" @click="showEditModal = false" class="btn btn-secondary">Cancel</button>
            <button type="submit" class="btn btn-primary" :disabled="saving">
              {{ saving ? 'Saving...' : 'Save Changes' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const employees = ref([])
const loading = ref(true)
const saving = ref(false)
const error = ref('')
const success = ref('')
const userEmail = ref(localStorage.getItem('user_email') || '')

const showCreateModal = ref(false)
const showEditModal = ref(false)

const newEmployee = ref({ name: '', email: '', location: '' })
const editingEmployee = ref({ id: null, name: '', email: '', location: '' })

const getAuthHeaders = () => ({
  'Content-Type': 'application/json',
  'Authorization': `Bearer ${localStorage.getItem('jwt_token')}`
})

const fetchEmployees = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await fetch('/api/employees', {
      headers: getAuthHeaders()
    })
    if (response.status === 403) {
      logout()
      return
    }
    if (!response.ok) throw new Error('Failed to fetch employees')
    employees.value = await response.json()
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}

const createEmployee = async () => {
  saving.value = true
  error.value = ''
  try {
    const response = await fetch('/api/employees', {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(newEmployee.value)
    })
    if (!response.ok) throw new Error('Failed to create employee')
    showCreateModal.value = false
    newEmployee.value = { name: '', email: '', location: '' }
    success.value = 'Employee created successfully!'
    setTimeout(() => success.value = '', 3000)
    fetchEmployees()
  } catch (err) {
    error.value = err.message
  } finally {
    saving.value = false
  }
}

const editEmployee = (employee) => {
  editingEmployee.value = { ...employee }
  showEditModal.value = true
}

const updateEmployee = async () => {
  saving.value = true
  error.value = ''
  try {
    const response = await fetch(`/api/employees/${editingEmployee.value.id}`, {
      method: 'PUT',
      headers: getAuthHeaders(),
      body: JSON.stringify({
        name: editingEmployee.value.name,
        email: editingEmployee.value.email,
        location: editingEmployee.value.location
      })
    })
    if (!response.ok) throw new Error('Failed to update employee')
    showEditModal.value = false
    success.value = 'Employee updated successfully!'
    setTimeout(() => success.value = '', 3000)
    fetchEmployees()
  } catch (err) {
    error.value = err.message
  } finally {
    saving.value = false
  }
}

const viewAudit = (employee) => {
  router.push(`/audit/Employee/${employee.id}`)
}

const logout = () => {
  localStorage.removeItem('jwt_token')
  localStorage.removeItem('user_email')
  router.push('/')
}

onMounted(fetchEmployees)
</script>

<style scoped>
.header-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.actions-bar {
  margin-bottom: 1.5rem;
}
</style>

