export default function Home() {
  return (
    <div className="min-h-screen flex flex-col">
      <header className="navbar bg-base-100 border-b border-base-200 px-6">
        <div className="flex-1">
          <span className="text-xl font-bold tracking-tight">Auth PDF Server</span>
        </div>
        <div className="flex-none gap-2">
          <button className="btn btn-primary btn-sm">Sign In</button>
        </div>
      </header>

      <main className="flex-1 max-w-5xl w-full mx-auto p-6 space-y-6">
        <div className="hero bg-base-200 rounded-box p-8">
          <div className="hero-content text-center">
            <div className="max-w-md">
              <h1 className="text-3xl font-bold">Document Portal</h1>
              <p className="py-4 text-base-content/80 text-sm">
                Securely authenticate, manage, and process PDF documents.
              </p>
              <div className="flex justify-center gap-3">
                <button className="btn btn-primary">Upload PDF</button>
                <button className="btn btn-outline">View Documents</button>
              </div>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div className="card bg-base-100 border border-base-200 shadow-xs">
            <div className="card-body p-5">
              <h2 className="card-title text-base font-semibold">Service Status</h2>
              <p className="text-sm text-base-content/70">Backend API operational on port 7001.</p>
              <div className="badge badge-success badge-sm mt-2">Active</div>
            </div>
          </div>

          <div className="card bg-base-100 border border-base-200 shadow-xs">
            <div className="card-body p-5">
              <h2 className="card-title text-base font-semibold">Storage Engine</h2>
              <p className="text-sm text-base-content/70">MinIO S3 bucket ready for uploads.</p>
              <div className="badge badge-info badge-sm mt-2">Connected</div>
            </div>
          </div>

          <div className="card bg-base-100 border border-base-200 shadow-xs">
            <div className="card-body p-5">
              <h2 className="card-title text-base font-semibold">Authentication</h2>
              <p className="text-sm text-base-content/70">JWT session authentication active.</p>
              <div className="badge badge-ghost badge-sm mt-2">Ready</div>
            </div>
          </div>
        </div>
      </main>

      <footer className="footer footer-center p-4 bg-base-100 border-t border-base-200 text-base-content/60 text-xs">
        <aside>
          <p>© 2026 Auth PDF Server. Built with Next.js & DaisyUI.</p>
        </aside>
      </footer>
    </div>
  );
}
