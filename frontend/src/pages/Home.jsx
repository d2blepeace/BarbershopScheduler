import { useNavigate } from 'react-router-dom'
import homeBg from '../assets/image/home-bg.jpg'

export default function Home() {
    const navigate = useNavigate()

    return (
        <div
            className="relative min-h-screen w-full bg-cover bg-center"
            style={{ backgroundImage: `url(${homeBg})` }}
        >
            {/* gradients ... unchanged */}
            <div className="pointer-events-none absolute inset-x-0 top-0 h-2/5 bg-gradient-to-b from-black/85 via-black/40 to-transparent" />
            <div className="pointer-events-none absolute inset-x-0 bottom-0 h-2/5 bg-gradient-to-t from-black/85 via-black/40 to-transparent" />

            <div className="relative z-10 flex min-h-screen flex-col items-center justify-between px-6 py-14">
                <h1 className="font-niagara text-center text-5xl leading-tight tracking-wider text-brand-gold md:text-7xl">
                    VINTAGE RAZOR
                    <br />
                    BARBER &amp; SALON
                </h1>

                <button
                    onClick={() => navigate('/book')}
                    className="font-niagara cursor-pointer border-2 border-brand-gold bg-black/50 px-14 py-3 text-3xl tracking-wider text-brand-gold transition-colors duration-150
                        hover:border-white hover:bg-black hover:text-white
                        active:border-brand-gold active:bg-brand-gold active:text-black
                        md:text-4xl"
                >
                    Book appointment
                </button>

                <div className="space-y-1 text-center text-lg text-white">
                    <p>vintagerazorbarbersalon@email.com</p>
                    <p>(408)-xxx-xxx</p>
                    <p>143 Street Blvd. San Jose CA.</p>
                </div>
            </div>
        </div>
    )
}