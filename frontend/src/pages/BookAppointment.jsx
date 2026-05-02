import { useEffect,  useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar.jsx';
import {api} from '../services/api.jsx';

// Map service name to local image.
import haircutImg from '../assets/image/haircutImg.jpg';
import menHaircutImg from '../assets/image/menHaircutImg.jpg';
import hairAndBeardImg from '../assets/image/hairAndBeardImg.jpg';
import kidImg from '../assets/image/kidImg.jpg';
import womenHaircutImg from '../assets/image/womenHairImg.jpg';
import womenColorImg from '../assets/image/womenColorImg.jpg';
import quickNailImg from '../assets/image/quickNailImg.jpg';
import nailColorImg from '../assets/image/nailColorImg.jpg';

// const for image of service buttons
const serviceImages = {
    'Hair cut': haircutImg,
    'Men hair cut & Shaving': menHaircutImg,
    'Men hair cut & Beard trim': hairAndBeardImg,
    'Kid hair cut': kidImg,
    'Women hair cut & Styling': womenHaircutImg,
    'Women hair cut & Color': womenColorImg,
    'Quick nail service': quickNailImg,
    'Nail manicure & Coloring': nailColorImg
}

// work hours at the top
const workHours = [
    { day: 'Sunday', time: 'Closed' },
    { day: 'Monday', time: '9:00 - 20:00' },
    { day: 'Tuesday', time: '9:00 - 20:00' },
    { day: 'Wednesday', time: '9:00 - 20:00' },
    { day: 'Thursday', time: '9:00 - 20:00' },
    { day: 'Friday', time: '9:00 - 20:00' },
    { day: 'Saturday', time: 'Closed' },
]

export default function BookAppointment() {
    const navigate = useNavigate()
    const [services, setServices] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)

    useEffect(() => {
        api.getServices()
            .then(data => setServices(data))
            .catch(err => setError(err.message))
            .finally(() => setLoading(false))
    }, [])
    
    const handleSelectService = (serviceId) => {
        navigate(`/book/slots/${serviceId}`)
    }
    
    return (
        <div className="min-h-screen bg-black text-white">
        <Navbar />

        <main className="px-6 py-10">
            <div className="mx-auto max-w-6xl rounded-lg border border-brand-gold p-10">

            <div className="mx-auto mb-6 flex h-24 w-24 items-center justify-center rounded-full border-2 border-brand-gold text-center text-[10px] leading-tight text-brand-gold">
                Vintage Razor<br />Barber &amp; Salon<br />Logo
            </div>

            <h1 className="font-niagara mb-8 text-center text-5xl tracking-wider text-brand-gold md:text-6xl">
                - Vintage Razor Barber &amp; Salon -
            </h1>

            <div className="mx-auto mb-10 max-w-md">
                {workHours.map(({ day, time }) => (
                <div key={day} className="flex justify-between py-1 text-lg">
                    <span>{day}</span>
                    <span>{time}</span>
                </div>
                ))}
            </div>

            <hr className="mb-8 border-brand-gold/60" />
            
            {/* Services heading */}
            <h2 className="font-niagara mb-10 text-center text-6xl tracking-wider text-brand-gold">
                Services
            </h2>

            {loading && <p className="text-center">Loading services...</p>}
            {error && <p className="text-center text-red-400">Failed to load: {error}</p>}
            
            {/* Service buttons section */}
            {!loading && !error && (
            <div className="space-y-10">
                {['Hair', 'Nail'].map(type => {
                const filtered = services.filter(s => s.type === type)

                if (filtered.length === 0) return null
                return (
                    <div key={type}>
                    {/* Section header */}
                    <h3 className="font-niagara mb-6 text-4xl tracking-wider text-brand-gold">
                        {type === 'Hair' ? 'Hair' : 'Nail'}
                    </h3>

                    {/* Service buttons for each type */}
                    <div className="grid gap-6 md:grid-cols-2">
                        {filtered.map((service) => {
                        const img = serviceImages[service.serviceName] || haircutImg

                        return (
                            <button
                            key={service.serviceId}
                            onClick={() => handleSelectService(service.serviceId)}

                            className="group flex cursor-pointer items-center gap-4 rounded-xl border-2 border-brand-gold bg-black p-3 text-left transition-colors duration-150
                                        hover:border-white hover:bg-black/70
                                        active:border-brand-gold active:bg-brand-gold"
                            >

                            <img
                                src={img}
                                alt={service.serviceName}
                                className="h-20 w-20 flex-shrink-0 rounded-md object-cover"
                            />
                            <div className="flex flex-col gap-2">
                                <h3 className="text-xl text-brand-gold transition-colors group-hover:text-white group-active:text-black">
                                {service.serviceName}
                                </h3>

                                <p className="text-base text-white transition-colors group-active:text-black">
                                {service.duration} mins | ${service.price}
                                </p>
                            </div>
                            </button>
                        )
                        })}
                    </div>
                    </div>
                )
                })}
            </div>
            )}
            </div>
        </main>
        </div>
    )
}