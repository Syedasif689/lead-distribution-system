type StatusMessageProps = {
  kind: "success" | "error" | "info";
  message: string;
};

export function StatusMessage({ kind, message }: StatusMessageProps) {
  const styles = {
    success: "border-emerald-200 bg-emerald-50 text-emerald-800",
    error: "border-red-200 bg-red-50 text-red-800",
    info: "border-slate-200 bg-white text-slate-700"
  };

  return <div className={`rounded-md border px-3 py-2 text-sm ${styles[kind]}`}>{message}</div>;
}
