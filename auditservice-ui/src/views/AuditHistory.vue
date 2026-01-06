<template>
  <div class="container">
    <router-link to="/employees" class="back-link">
      ← Back to Employees
    </router-link>

    <div class="header">
      <h1>📋 Audit History</h1>
      <span class="user-badge">Employee #{{ entityId }}</span>
    </div>

    <div v-if="error" class="alert alert-error">
      {{ error }}
    </div>

    <div class="card">
      <div v-if="loading" class="empty-state">
        <p>Loading audit history...</p>
      </div>

      <div v-else-if="!auditData || auditData.length === 0" class="empty-state">
        <p>No audit history found</p>
      </div>

      <div v-else class="audit-list">
        <div v-for="(audit, index) in auditData" :key="index" class="audit-item">
          <div class="audit-header">
            <span :class="['audit-action', getAction(audit)]">{{ getAction(audit) }}</span>
            <span class="audit-time">{{ formatDate(audit.changed_at) }}</span>
          </div>
          <div class="audit-user">Changed by: <strong>{{ audit.changed_by }}</strong></div>
          <div class="audit-request-id">Request ID: {{ audit.request_id }}</div>
          
          <div v-for="(entity, eIndex) in audit.entities" :key="eIndex">
            <div v-if="entity.changes && entity.changes.length > 0" class="audit-changes">
              <div v-for="(change, cIndex) in entity.changes" :key="cIndex" class="change-item">
                <span class="change-field">{{ change.field }}:</span>
                <span v-if="change.old_value" class="change-old">{{ change.old_value }}</span>
                <span v-if="change.old_value"> → </span>
                <span class="change-new">{{ change.new_value }}</span>
              </div>
            </div>

            <div v-else-if="entity.action === 'CREATE'" class="audit-changes">
              <div class="change-item">
                <span class="change-new">New record created</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const entityId = ref(route.params.id)
const auditData = ref([])
const loading = ref(true)
const error = ref('')

const getAuthHeaders = () => ({
  'Content-Type': 'application/json',
  'Authorization': `Bearer ${localStorage.getItem('jwt_token')}`
})

const fetchAuditHistory = async () => {
  loading.value = true
  error.value = ''
  try {
    const response = await fetch(`/api/audit/entity/employees/${entityId.value}`, {
      headers: getAuthHeaders()
    })
    if (!response.ok) throw new Error('Failed to fetch audit history')
    const data = await response.json()
    auditData.value = data.history || []
  } catch (err) {
    error.value = err.message
  } finally {
    loading.value = false
  }
}

const getAction = (audit) => {
  if (audit.entities && audit.entities.length > 0) {
    return audit.entities[0].action
  }
  return 'UNKNOWN'
}

const formatDate = (timestamp) => {
  if (!timestamp) return 'N/A'
  const date = new Date(timestamp)
  return date.toLocaleString()
}

onMounted(fetchAuditHistory)
</script>

<style scoped>
.audit-request-id {
  color: #666;
  font-size: 0.8rem;
  margin-bottom: 0.5rem;
  font-family: 'Monaco', 'Menlo', monospace;
}
</style>
