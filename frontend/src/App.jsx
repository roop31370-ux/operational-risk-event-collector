import { useState } from 'react'
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className="min-h-screen bg-gray-100 font-sans">
      {/* Navigation Bar */}
      <nav className="bg-blue-700 text-white p-4 shadow-md">
        <div className="container mx-auto flex justify-between items-center">
          <h1 className="text-2xl font-bold">Tool-66: Risk Collector</h1>
          <div className="space-x-4">
            <button className="hover:underline">Dashboard</button>
            <button className="hover:underline">Report Event</button>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <header className="bg-white py-12 border-b">
        <div className="container mx-auto text-center">
          <h2 className="text-4xl font-extrabold text-gray-800 mb-4">
            Welcome to Operational Risk Management
          </h2>
          <p className="text-lg text-gray-600 max-w-2xl mx-auto">
            Identify, track, and mitigate operational risks effectively using our centralized event collector.
          </p>
        </div>
      </header>

      {/* Main Content Area */}
      <main className="container mx-auto py-10 px-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
          
          {/* Quick Stats Card */}
          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
            <h3 className="text-xl font-semibold mb-4 text-blue-700">Quick Stats</h3>
            <p className="text-gray-700 mb-6">Track your reported events in real-time.</p>
            <button 
              onClick={() => setCount((count) => count + 1)}
              className="bg-blue-600 text-white px-6 py-2 rounded hover:bg-blue-700 transition-colors"
            >
              Total Events Reviewed: {count}
            </button>
          </div>

          {/* Action Card */}
          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
            <h3 className="text-xl font-semibold mb-4 text-green-700">Documentation</h3>
            <p className="text-gray-700 mb-4">Need help getting started with the Tool-66 project?</p>
            <a 
              href="https://react.dev/" 
              target="_blank" 
              className="text-blue-600 font-medium hover:underline"
            >
              View Developer Guide →
            </a>
          </div>

        </div>
      </main>

      {/* Footer */}
      <footer className="mt-auto py-6 bg-gray-800 text-white text-center">
        <p>© 2026 Operational Risk Event Collector | Capstone Project</p>
      </footer>
    </div>
  )
}

export default App