const BASE_URL = 'http://localhost:8080'

async function request(path, options = {}) {
    const response = await fetch (`${BASE_URL}${path}`, {
        headers: { 'Content-Type': 'application/json'},
        ...options,    
    })

    if (!response.ok) {
        const error = await response.text() 
            throw new Error('API error ${response.status}: ${error}')
    }

    // Some endpoints return to body (204)
    if (response.status === 204) return null
    return response.json()
}

export const api = {
    //Services
    getServices: () => request('/services'),

    //Slots
    getSlots: (params) => {
        const query = new URLSearchParams(params).toString()
        return request(`/slots?${query}`)
    },
    
    // Appointments
    bookAppointment: (data) =>
        request('/appointments', {
        method: 'POST',
        body: JSON.stringify(data),
        }),

    cancelAppointment: (id) =>
        request(`/appointments/${id}`, { method: 'DELETE' }),
}