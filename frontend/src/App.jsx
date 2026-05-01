import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import BookAppointment from './pages/BookAppointment'
import Availability from './pages/Availability'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/book" element={<BookAppointment />} />
        <Route path="/book/slots/:serviceId" element={<Availability />} />
        <Route path="/confirmation" element={<div className="min-h-screen bg-black p-10 text-white">Booking confirmed — confirmation page coming next</div>} />
      </Routes>
    </BrowserRouter>
  )
}