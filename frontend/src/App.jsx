import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import BookAppointment from './pages/BookAppointment'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/book" element={<BookAppointment />} />
        <Route path="/book/slots/:serviceId" element={<div className="p-10 text-white">Slot selection — coming next</div>} />
      </Routes>
    </BrowserRouter>
  )
}