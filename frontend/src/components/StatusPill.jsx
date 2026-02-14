export default function StatusPill({ text, tone = "neutral" }) {
  return <span className={`pill pill-${tone}`}>{text}</span>;
}
