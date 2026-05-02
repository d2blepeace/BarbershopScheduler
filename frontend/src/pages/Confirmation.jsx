import { useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import Navbar from '../components/Navbar.jsx'
import { api } from '../services/api.jsx'

export default function Confirmation() {
    const navigate = useNavigate()
    const location = useLocation()
    const[showCancelModal, setShowCancelModal] = useState(false)
    const[cancelling, setCancelling] = useState(false)
    const {appointment, service, provider, slot, customerName} = location.state


    // Bail out if user lands on confirmation without booking
    if (!location.state) {
        return (
        <div className="min-h-screen bg-black text-white">
            <Navbar />
            <div className="p-10 text-center">
            <p className="mb-4">No booking information found.</p>
            <button
                onClick={() => navigate('/')}
                className="font-niagara cursor-pointer border-2 border-brand-gold px-8 py-2 text-brand-gold hover:bg-brand-gold hover:text-black"
            >
                Back to Home
            </button>
            </div>
        </div>
        )
    }
    const handleChange = () => {
        // Cancel current booking silently, then go reroll from service selection
        api.cancelAppointment(appointment.appointmentId).catch(() => {})
        navigate('/book')
    }

    const handleCancelConfirm = async () => {
        setCancelling(true)

        try {
            await api.cancelAppointment(appointment.appointmentId)
            setShowCancelModal(true)
        } catch (err) {
            alert('Failed to cancel: ' + err.message)
        } finally {
            setCancelling(false)
        }
    }

    const formatDate = (dateStr) => {
        const date = new Date(dateStr)
        return date.toLocaleDateString('en-US', {month: 'long', day: 'numeric', year: 'numeric'})
    }

    return (
        <div className="min-h-screen bg-black text-white">
        <Navbar />

        <main className="px-6 py-10">
            <div className="mx-auto max-w-3xl rounded-lg border border-brand-gold p-10">
            {/* Title */}
            <h1 className="font-niagara mb-8 text-center text-6xl tracking-wider text-brand-gold">
                Booking confirmation
            </h1>

            {/* Thank you card */}
            <div className="mb-10 rounded-lg border-2 border-white p-12 text-center">
                <p style={{ fontFamily: 'Miama, cursive' }} className="text-7xl text-white">
                Thank you!
                </p>
            </div>

            <hr className="mb-8 border-brand-gold/60" />

            {/* Booking details */}
            <div className="mb-10 space-y-6 text-center">
                <div>
                <h2 className="font-niagara text-2xl text-brand-gold">Appointment</h2>
                <p>#{appointment.appointmentId}</p>
                </div>

                <div>
                <h2 className="font-niagara text-2xl text-brand-gold">Service</h2>
                <p>{service.serviceName}</p>
                <p>{service.duration} mins | ${service.price}</p>
                </div>

                <div>
                <h2 className="font-niagara text-2xl text-brand-gold">Date/Time</h2>
                <p>{formatDate(slot.date)} - {slot.time.substring(0, 5)}</p>
                </div>

                <div>
                <h2 className="font-niagara text-2xl text-brand-gold">Team member</h2>
                <p>{provider.name}</p>
                </div>

                <div>
                <h2 className="font-niagara text-2xl text-brand-gold">Customer</h2>
                <p>{customerName}</p>
                </div>
            </div>

            {/* Buttons */}
            <div className="flex justify-between gap-4">
                <button
                onClick={handleChange}
                className="font-niagara cursor-pointer rounded-lg border-2 border-brand-gold bg-black px-12 py-3 text-3xl tracking-wider text-brand-gold transition-colors
                            hover:border-white hover:bg-black hover:text-white
                            active:border-brand-gold active:bg-brand-gold active:text-black"
                >
                Change
                </button>
                <button
                onClick={handleCancelConfirm}
                disabled={cancelling}
                className="font-niagara cursor-pointer rounded-lg border-2 border-white bg-black px-12 py-3 text-3xl tracking-wider text-white transition-colors
                            hover:border-brand-gold hover:text-brand-gold
                            active:bg-white active:text-black
                            disabled:opacity-50"
                >
                {cancelling ? 'Cancelling...' : 'Cancel'}
                </button>
            </div>
            </div>
        </main>

        {/* Cancellation modal */}
        {showCancelModal && (
            <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/80">
            <div className="mx-6 max-w-md rounded-lg border-2 border-brand-gold bg-black p-10 text-center">
                <h2 className="font-niagara mb-6 text-3xl tracking-wider text-brand-gold">
                Appointment cancelled successfully
                </h2>
                <button
                onClick={() => navigate('/')}
                className="font-niagara cursor-pointer rounded-lg border-2 border-brand-gold bg-black px-10 py-3 text-2xl tracking-wider text-brand-gold transition-colors
                            hover:border-white hover:bg-black hover:text-white
                            active:border-brand-gold active:bg-brand-gold active:text-black"
                >
                Back to Home
                </button>
            </div>
            </div>
        )}
        </div>
    )
}

