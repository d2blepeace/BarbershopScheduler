import { useState } from "react";

//Day of week
const DAYS = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']

export default function Calendar({selectedDate, onSelectDate}) {
    const [view, setView] = useState( () => {
        const d = new Date() 
        return new Date(d.getFullYear(), d.getMonth(), 1)
    }) 

    const today = new Date()
    today.setHours(0, 0, 0, 0)

    const year = view.getFullYear()
    const month = view.getMonth()
    const monthName = view.toLocaleString('default', {month: 'long'})
    const firstDayOfMonth = new Date(year, month, 1).getDay()
    const daysInMonth = new Date(year, month + 1, 0).getDate()

    const cells =[]
    for (let i = 0; i < firstDayOfMonth; i++) cells.push(null)
    for (let d = 0; d <= daysInMonth; d++) cells.push(d);

    const isPast = (day) => new Date(year, month, day) < today
    
    const isSelected = (day) =>
        selectedDate && selectedDate.getFullYear() === year &&
        selectedDate.getMonth() === month && 
        selectedDate.getDate() === day

    return (
        <div className="w-full">
        <div className="mb-4 flex items-center justify-between">
            <button
            onClick={() => setView(new Date(year, month - 1, 1))}
            className="cursor-pointer px-2 text-2xl text-brand-gold hover:text-white"
            >‹</button>
            <h3 className="text-2xl text-white">{monthName} {year}</h3>
            <button
            onClick={() => setView(new Date(year, month + 1, 1))}
            className="cursor-pointer px-2 text-2xl text-brand-gold hover:text-white"
            >›</button>
        </div>

        <div className="mb-2 grid grid-cols-7 rounded-md border border-brand-gold py-2">
            {DAYS.map(d => (
            <div key={d} className="text-center text-sm text-brand-gold">{d}</div>
            ))}
        </div>

        <div className="grid grid-cols-7 gap-1">
            {cells.map((day, i) => {
            if (day === null) return <div key={i} />
            const past = isPast(day)
            const selected = isSelected(day)
            return (
                <button
                key={i}
                onClick={() => !past && onSelectDate(new Date(year, month, day))}
                disabled={past}
                className={`
                    aspect-square rounded text-base transition-colors
                    ${past
                    ? 'cursor-not-allowed text-gray-600'
                    : 'cursor-pointer text-white hover:bg-brand-gold/20'}
                    ${selected ? '!bg-brand-gold !text-black' : ''}
                `}
                >
                {day}
                </button>
            )
            })}
        </div>
        </div>
    )
}