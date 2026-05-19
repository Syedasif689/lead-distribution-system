import type { Metadata } from "next";
import type { ReactNode } from "react";
import Link from "next/link";
import "./globals.css";

export const metadata: Metadata = {
  title: "Prowider Lead Distribution",
  description: "Mini lead distribution system with fair allocation and real-time provider updates"
};

export default function RootLayout({ children }: Readonly<{ children: ReactNode }>) {
  return (
    <html lang="en">
      <body>
        <header className="border-b border-slate-200 bg-white">
          <nav className="mx-auto flex max-w-6xl flex-wrap items-center justify-between gap-3 px-4 py-4">
            <Link href="/dashboard" className="text-lg font-semibold tracking-normal text-slate-950">
              Prowider
            </Link>
            <div className="flex gap-2 text-sm">
              <Link className="rounded-md border border-slate-300 px-3 py-2 hover:bg-slate-50" href="/request-service">
                Request Service
              </Link>
              <Link className="rounded-md border border-slate-300 px-3 py-2 hover:bg-slate-50" href="/dashboard">
                Dashboard
              </Link>
              <Link className="rounded-md border border-slate-300 px-3 py-2 hover:bg-slate-50" href="/test-tools">
                Test Tools
              </Link>
            </div>
          </nav>
        </header>
        <main className="mx-auto max-w-6xl px-4 py-6">{children}</main>
      </body>
    </html>
  );
}
