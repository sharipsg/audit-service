<template>
  <div class="login-page">
    <div class="login-container">
      <div class="card login-card">
        <div class="logo">
          <div class="logo-icon">🔐</div>
          <h1>Audit Service</h1>
          <p class="subtitle">Enter your email to get started</p>
        </div>

        <div v-if="error" class="alert alert-error">
          {{ error }}
        </div>

        <form @submit.prevent="generateToken">
          <div class="form-group">
            <label for="email">Email Address</label>
            <input
              id="email"
              v-model="email"
              type="email"
              class="input"
              placeholder="you@example.com"
              required
            />
          </div>

          <button type="submit" class="btn btn-primary btn-full" :disabled="loading">
            {{ loading ? 'Generating...' : 'Generate Token' }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const email = ref('')
const error = ref('')
const loading = ref(false)

const generateToken = async () => {
  error.value = ''
  loading.value = true

  try {
    const response = await fetch('/api/auth/token', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ email: email.value })
    })

    if (!response.ok) {
      throw new Error('Failed to generate token')
    }

    const data = await response.json()
    localStorage.setItem('jwt_token', data.token)
    localStorage.setItem('user_email', data.email)
    router.push('/employees')
  } catch (err) {
    error.value = err.message || 'Failed to generate token'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
}

.login-container {
  width: 100%;
  max-width: 420px;
}

.login-card {
  text-align: center;
}

.logo {
  margin-bottom: 2rem;
}

.logo-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.logo h1 {
  font-size: 1.75rem;
  margin-bottom: 0.5rem;
}

.subtitle {
  color: #888;
  font-size: 0.95rem;
}

.btn-full {
  width: 100%;
  margin-top: 0.5rem;
}
</style>

