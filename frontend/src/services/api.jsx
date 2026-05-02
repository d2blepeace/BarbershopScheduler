const BASE_URL = 'http://localhost:8080'

async function request(path, options = {}) {
    const response = await fetch (`${BASE_URL}${path}`, {
        headers: { 'Content-Type': 'application/json'},
        ...options,    
    })

    if (!response.ok) {
        const error = await response.text() 
            throw new Error(`API error ${response.status}: ${error}`)
    }

    // Some endpoints return to body (204)
    if (response.status === 204) return null
    return response.json()
}

export const api = {
    //Services
    getServices: () => request('/services'),

    //Providers
    getProviders: () => request('/providers'),

    //Slots
    getSlots: (providerId, date) => request(`/slots?providerId=${providerId}&date=${date}`),

    // Appointments
    bookAppointment: (data) =>
        request('/appointments', {
        method: 'POST',
        body: JSON.stringify(data),
        }),

    cancelAppointment: (id) =>
        request(`/appointments/${id}/cancel`, { method: 'PUT' }),
}