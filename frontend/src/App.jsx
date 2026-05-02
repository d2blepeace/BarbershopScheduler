import { BrowserRouter, Routes, Route } from 'react-router-dom'
import Home from './pages/Home'
import BookAppointment from './pages/BookAppointment'
import Availability from './pages/Availability'
import Confirmation from './pages/Confirmation'

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/book" element={<BookAppointment />} />
        <Route path="/book/slots/:serviceId" element={<Availability />} />
        <Route path="/confirmation" element={<Confirmation />} />
      </Routes>
    </BrowserRouter>
  )
}