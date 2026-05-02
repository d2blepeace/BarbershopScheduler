import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import Navbar from '../components/Navbar.jsx'
import Calendar from '../components/Calendar.jsx'
import { api } from '../services/api.jsx'

import luneImg from '../assets/image/lune.jpg'
import gustaveImg from '../assets/image/gustave.jpg'
import maelleImg from '../assets/image/maelle.jpg'
import monocoImg from '../assets/image/monoco.jpg'
import versoImg from '../assets/image/verso.jpg'
import scielImg from '../assets/image/sciel.jpg'
import backArrow from '../assets/image/back-button.png'

export default function Availability() {
    const navigate = useNavigate()
    const { serviceId } = useParams()
    const [service, setService] = useState(null)
    const [providers, setProviders] = useState([])
    const [selectedProvider, setSelectedProvider] = useState(null)
    const [selectedDate, setSelectedDate] = useState(null)
    const [slots, setSlots] = useState([])
    const [selectedSlot, setSelectedSlot] = useState(null)
    const [loading, setLoading] = useState(true)
    const [booking, setBooking] = useState(false)
    const [error, setError] = useState(null)

    // provider images
    const providerImages = {
        'Lune': luneImg,
        'Gustave': gustaveImg,
        'Maelle': maelleImg,
        'Monoco': monocoImg,
        'Verso': versoImg,
        'Sciel': scielImg,
    }

    // Load providers and identify the chosen service from the URL
    useEffect(() => {
        Promise.all([api.getServices(), api.getProviders()])
            .then(([services, providers]) => {
                setProviders(providers) 

                const s = services.find(svc => String(svc.serviceId) === serviceId)

                setService(s)
            })
            .catch(err => setError(err.message))
            .finally(() => setLoading(false))
    }, [serviceId])

    // Fetch slots when provider AND date are both chosen
    useEffect(() => {
        if (!selectedProvider || !selectedDate) return
        setSelectedSlot(null)

        const dateStr = formatDate(selectedDate)

        api.getSlots(selectedProvider.providerId, dateStr)
            .then(setSlots)
            .catch(err => setError(err.message))
    }, [selectedProvider, selectedDate])

    const handleConfirm = async () => {
        if (!selectedSlot || !service) return
        setBooking(true)

        try {
            await api.bookAppointment({
                // hardcoded for demo (no login system yet)
                customerId: 1,
                serviceId: service.serviceId,
                slotId: selectedSlot.slotId,
                notes: '',
            })
            navigate('/confirmation')
        } catch (err) {
            alert('Booking failed: ' + err.message)
        } finally {
            setBooking(false)
        }
    }

    if (loading) return <div className="min-h-screen bg-black p-10 text-white">Loading...</div>

    return (
        <div className="min-h-screen bg-black text-white">
            <Navbar />

            <main className="px-6 py-10">
                <div className="mx-auto max-w-6xl rounded-lg border border-brand-gold p-10">

                    {/* Top section: provider picker */}
                    <div className="relative mb-6">
                        <button
                            onClick={() => navigate(-1)}
                            className="absolute left-0 top-0 cursor-pointer text-3xl text-brand-gold hover:text-white"
                        >
                            <img src={backArrow} alt="Back to services" className="h-12 w-12" />
                        </button>
                        <h2 className="font-niagara text-center text-6xl text-brand-gold">
                            Select a team member
                        </h2>
                    </div>

                    <div className="mb-10 grid grid-cols-2 gap-6 md:grid-cols-4">
                        {providers.map(p => {
                            const isMatch = 
                                (service?.type === 'Hair' && p.bio === 'Barber') ||
                                (service?.type === 'Nail' && p.bio === 'Nail Technician')

                            const disabled = !p.active || !isMatch
                            const selected = selectedProvider?.providerId === p.providerId
                            
                            return (
                                <button
                                    key={p.providerId}
                                    disabled={disabled}
                                    onClick={() => setSelectedProvider(p)}
                                    className={`
                                            flex flex-col items-center gap-3 rounded-xl border-2 p-6 transition-colors
                                            ${disabled
                                            ? 'cursor-not-allowed border-brand-gold/30 opacity-40'
                                            : 'cursor-pointer border-brand-gold hover:border-white hover:bg-black/70'}
                                            ${selected ? '!border-brand-gold !bg-brand-gold' : ''}
                                            `}
                                >
                                    <img
                                        src={providerImages[p.name]}
                                        alt={p.name}
                                        className="h-24 w-24 rounded-full object-cover object-top"
                                    />
                                    <div className="text-center">
                                        <div className={`font-niagara text-2xl ${selected ? 'text-black' : 'text-brand-gold'}`}>
                                            {p.name}
                                        </div>
                                        <div className={`text-sm ${selected ? 'text-black' : 'text-white'}`}>
                                            ({p.bio})
                                        </div>
                                    </div>
                                </button>
                            )
                        })}
                    </div>

                    {/* Bottom section only appears once a provider is selected */}
                    {selectedProvider && (
                        <>
                            <hr className="mb-8 border-brand-gold/60" />
                            <h2 className="font-niagara mb-8 text-center text-6xl text-brand-gold">
                                Select date and time
                            </h2>

                            <div className="grid gap-8 md:grid-cols-2">
                                <Calendar
                                    selectedDate={selectedDate}
                                    onSelectDate={setSelectedDate}
                                />

                                <div>
                                    {!selectedDate && (
                                        <p className="text-center text-gray-400">Pick a date first.</p>
                                    )}

                                    {selectedDate && slots.length === 0 && (
                                        <p className="text-center text-gray-400">No available slots for this date.</p>
                                    )}

                                    {selectedDate && slots.length > 0 && (
                                        <div className="grid grid-cols-2 gap-3">
                                            {slots.map(slot => {
                                                const selected = selectedSlot?.slotId === slot.slotId
                                                return (
                                                    <button
                                                        key={slot.slotId}
                                                        onClick={() => setSelectedSlot(slot)}
                                                        className={`cursor-pointer rounded-lg border-2 px-6 py-3 text-xl transition-colors${selected
                                                            ? '!border-brand-gold !bg-brand-gold !text-black'
                                                            : 'border-brand-gold bg-black text-white hover:border-white hover:bg-black/70'}
                                                        `}
                                                    >
                                                        {formatTime(slot.time)}
                                                    </button>
                                                )
                                            })}
                                        </div>
                                    )}
                                </div>
                            </div>

                            {/* Total + Confirm */}
                            {selectedSlot && (
                                <div className="mt-10 text-center">
                                    <p className="mb-4 text-2xl text-white">
                                        <span className="font-niagara text-5xl text-brand-gold">Total: </span>
                                        {service.serviceName} - ${service.price}
                                    </p>
                                    <button
                                        onClick={handleConfirm}
                                        disabled={booking}
                                        className="font-niagara cursor-pointer rounded-lg border-2 border-brand-gold bg-black px-24 py-5 text-5xl tracking-wider text-brand-gold transition-colors
                                            hover:border-white hover:bg-black hover:text-white
                                            active:border-brand-gold active:bg-brand-gold active:text-black
                                            disabled:opacity-50"
                                    >
                                        {booking ? 'Booking...' : 'Confirm'}
                                    </button>
                                </div>
                            )}
                        </>
                    )}

                    {error && <p className="mt-4 text-center text-red-400">{error}</p>}
                </div>
            </main>
        </div>
    )
}

// Helpers
function formatDate(d) {
    const yyyy = d.getFullYear()
    const mm = String(d.getMonth() + 1).padStart(2, '0')
    const dd = String(d.getDate()).padStart(2, '0')
    return `${yyyy}-${mm}-${dd}`
}

function formatTime(timeStr) {
    // Backend sends "09:00:00", strip seconds
    return timeStr.substring(0, 5)
}