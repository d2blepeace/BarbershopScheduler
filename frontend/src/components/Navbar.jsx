import { useNavigate } from 'react-router-dom'

export default function Navbar() {
    const navigate = useNavigate()

    return (
        <nav className="sticky top-0 z-50 border-b border-brand-gold bg-black">
            <div className="mx-auto flex max-w-7xl items-center justify-between px-8 py-4">
                {/* Left links — placeholders, not implemented */}
                <div className="font-niagara flex gap-12 text-2xl tracking-wider text-brand-gold">
                    <button className="cursor-not-allowed opacity-70" disabled>Login</button>
                    <button className="cursor-not-allowed opacity-70" disabled>Account</button>
                </div>

                {/* Logo — clickable, navigates home */}
                <button
                    onClick={() => navigate('/')}
                    className="font-niagara flex h-20 w-20 cursor-pointer items-center justify-center rounded-full border-2 border-brand-gold bg-black text-center text-[10px] leading-tight text-brand-gold transition-transform hover:scale-105"
                >
                    Vintage Razor<br />Barber &amp; Salon<br />Logo
                </button>

                {/* Right links — placeholders, not implemented */}
                <div className="font-niagara flex gap-12 text-2xl tracking-wider text-brand-gold">
                    <button className="cursor-not-allowed opacity-70" disabled>Reviews</button>
                    <button className="cursor-not-allowed opacity-70" disabled>Address</button>
                </div>
            </div>
        </nav>
    )
}