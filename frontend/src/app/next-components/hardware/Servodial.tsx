"use client"; // needed in Next.js App Router; harmless elsewhere

import { useCallback, useId, useRef, useState } from "react";

export type ServoDialProps = {
    /** Controlled value in degrees. Omit for uncontrolled. */
    value?: number;
    defaultValue?: number;
    min?: number;   // default 0
    max?: number;   // default 180
    step?: number;  // snap size in degrees, default 1
    disabled?: boolean;
    label?: string; // used for aria-label only
    onChange?: (angle: number) => void;
    /** Fires once when the user releases - good place to send to your API/ESP32 */
    onCommit?: (angle: number) => void;
};

// Shaft (pivot) position and sweep radius, in viewBox units
const CX = 140;
const CY = 205;
const R = 105;
const HORN_LEN = 96;

const clamp = (n: number, lo: number, hi: number) => Math.min(hi, Math.max(lo, n));

/** servo angle (0 = pointing left, 90 = up, 180 = right) -> point around the shaft */
function polar(angle: number, radius: number) {
    const rad = ((180 - angle) * Math.PI) / 180;
    return { x: CX + radius * Math.cos(rad), y: CY - radius * Math.sin(rad) };
}

function arcPath(from: number, to: number, radius: number) {
    const a = polar(from, radius);
    const b = polar(to, radius);
    return `M ${a.x} ${a.y} A ${radius} ${radius} 0 0 1 ${b.x} ${b.y}`;
}

export default function ServoDial({
    value,
    defaultValue = 90,
    min = 0,
    max = 180,
    step = 1,
    disabled = false,
    label = "Servo angle",
    onChange,
    onCommit,
}: ServoDialProps) {
    const [internal, setInternal] = useState(defaultValue);
    const angle = value ?? internal;
    const svgRef = useRef<SVGSVGElement>(null);
    const dragging = useRef(false);
    const [active, setActive] = useState(false);
    const uid = useId().replace(/:/g, ""); // unique gradient/filter ids per instance

    const update = useCallback(
        (next: number) => {
            const snapped = clamp(Math.round(next / step) * step, min, max);
            setInternal(snapped);
            onChange?.(snapped);
            return snapped;
        },
        [step, min, max, onChange]
    );

    const angleFromPointer = (e: React.PointerEvent) => {
        const svg = svgRef.current!;
        const pt = svg.createSVGPoint();
        pt.x = e.clientX;
        pt.y = e.clientY;
        const p = pt.matrixTransform(svg.getScreenCTM()!.inverse());

        const dx = p.x - CX;
        const dy = CY - p.y;
        let theta = (Math.atan2(dy, dx) * 180) / Math.PI; // 0 = right, 90 = up, 180 = left

        // Pointer below the shaft: snap to whichever end is closer
        if (theta < 0) theta = theta < -90 ? 180 : 0;

        return 180 - theta; // servo angle: 0 = left, 180 = right
    };

    const onPointerDown = (e: React.PointerEvent<SVGSVGElement>) => {
        if (disabled || e.button !== 0) return; // left click / primary touch only
        dragging.current = true;
        setActive(true);
        e.currentTarget.setPointerCapture(e.pointerId);
        update(angleFromPointer(e));
    };

    const onPointerMove = (e: React.PointerEvent<SVGSVGElement>) => {
        if (dragging.current) update(angleFromPointer(e));
    };

    const endDrag = (e: React.PointerEvent<SVGSVGElement>) => {
        if (!dragging.current) return;
        dragging.current = false;
        setActive(false);
        e.currentTarget.releasePointerCapture(e.pointerId);
        onCommit?.(angle);
    };

    const onKeyDown = (e: React.KeyboardEvent<SVGSVGElement>) => {
        if (disabled) return;
        const delta =
            e.key === "ArrowRight" || e.key === "ArrowUp" ? step :
            e.key === "ArrowLeft" || e.key === "ArrowDown" ? -step : 0;
        if (!delta) return;
        e.preventDefault();
        onCommit?.(update(angle + delta));
    };

    const ticks = [0, 30, 60, 90, 120, 150, 180];
    const scallops = Array.from({ length: 22 }, (_, i) => (i / 22) * Math.PI * 2);
    const hornRotation = -(180 - angle); // horn is drawn pointing right, then rotated

    return (
        <svg
            ref={svgRef}
            viewBox="0 0 280 390"
            width="100%"
            style={{
                maxWidth: 220,
                touchAction: "none",
                userSelect: "none",
                cursor: disabled ? "default" : active ? "grabbing" : "grab",
                opacity: disabled ? 0.6 : 1,
            }}
            role="slider"
            tabIndex={disabled ? -1 : 0}
            aria-label={label}
            aria-disabled={disabled}
            aria-valuemin={min}
            aria-valuemax={max}
            aria-valuenow={angle}
            aria-valuetext={`${angle} degrees`}
            onPointerDown={onPointerDown}
            onPointerMove={onPointerMove}
            onPointerUp={endDrag}
            onPointerCancel={endDrag}
            onKeyDown={onKeyDown}
        >
            <defs>
                <linearGradient id={`${uid}-body`} x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0" stopColor="#2a82cf" />
                    <stop offset="1" stopColor="#175a96" />
                </linearGradient>
                <linearGradient id={`${uid}-horn`} x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0" stopColor="#ffffff" />
                    <stop offset="1" stopColor="#d3dae6" />
                </linearGradient>
                <filter id={`${uid}-shadow`} x="-30%" y="-30%" width="160%" height="160%">
                    <feDropShadow dx="0" dy="2" stdDeviation="2" floodColor="#0a2a4d" floodOpacity="0.4" />
                </filter>
            </defs>

            {/* Cable into a black 3-pin connector */}
            <path d="M 118 48 C 118 75, 124 85, 124 108" fill="none" stroke="#6b3f24" strokeWidth="4.5" strokeLinecap="round" />
            <path d="M 140 48 C 140 75, 140 85, 140 108" fill="none" stroke="#c0392b" strokeWidth="4.5" strokeLinecap="round" />
            <path d="M 162 48 C 162 75, 156 85, 156 108" fill="none" stroke="#e8871e" strokeWidth="4.5" strokeLinecap="round" />
            <rect x="102" y="6" width="76" height="44" rx="4" fill="#1a1a1c" />
            {[108, 130, 152].map((x) => (
                <rect key={x} x={x} y="12" width="16" height="11" rx="1.5" fill="#e9edf2" />
            ))}

            {/* Case + mounting ears */}
            <rect x="84" y="100" width="112" height="272" rx="8" fill={`url(#${uid}-body)`} stroke="#0f3f70" strokeWidth="1.5" />
            <rect x="84" y="100" width="112" height="34" rx="8" fill="#175a96" opacity="0.55" />
            <rect x="84" y="338" width="112" height="34" rx="8" fill="#175a96" opacity="0.55" />
            {[[104, 117], [176, 117], [104, 355], [176, 355]].map(([x, y]) => (
                <g key={`${x}-${y}`}>
                    <circle cx={x} cy={y} r="7.5" fill="#0d1b2e" />
                    <circle cx={x} cy={y} r="7.5" fill="none" stroke="#6fb0ee" strokeWidth="1" opacity="0.7" />
                </g>
            ))}

            {/* Gear housing */}
            {scallops.map((a) => (
                <circle key={a} cx={CX + 40 * Math.cos(a)} cy={CY + 40 * Math.sin(a)} r="5.5" fill="#4e9fe4" />
            ))}
            <circle cx={CX} cy={CY} r="40" fill="#4e9fe4" />
            <circle cx={CX} cy={CY} r="30" fill="#2f78bf" />

            {/* Label sticker with the live angle */}
            <rect x="100" y="296" width="80" height="30" rx="4" fill="#1a1a1c" />
            <text
                x={CX}
                y="318"
                textAnchor="middle"
                fontSize="19"
                fill="#f4f7fb"
                style={{ fontVariantNumeric: "tabular-nums", fontWeight: 600 }}
            >
                {String(angle).padStart(3, "0")}°
            </text>

            {/* Sweep guide */}
            <path d={arcPath(0, 180, R)} fill="none" stroke="#8fa1bd" strokeOpacity="0.5" strokeWidth="3" strokeLinecap="round" />
            {angle > 0 && (
                <path d={arcPath(0, angle, R)} fill="none" stroke="#f29b2c" strokeWidth="3" strokeLinecap="round" />
            )}
            {ticks.map((t) => {
                const a = polar(t, R - 6);
                const b = polar(t, R + 6);
                return <line key={t} x1={a.x} y1={a.y} x2={b.x} y2={b.y} stroke="#8fa1bd" strokeWidth="1.5" />;
            })}
            <text x={CX - R - 16} y={CY + 4} textAnchor="middle" fontSize="10" fill="#5b6b82">0</text>
            <text x={CX + R + 16} y={CY + 4} textAnchor="middle" fontSize="10" fill="#5b6b82">180</text>

            {/* Single-arm servo horn */}
            <g transform={`rotate(${hornRotation} ${CX} ${CY})`} filter={`url(#${uid}-shadow)`}>
                <path
                    d={`M ${CX} ${CY - 12} L ${CX + HORN_LEN} ${CY - 6} A 6 6 0 0 1 ${CX + HORN_LEN} ${CY + 6} L ${CX} ${CY + 12} Z`}
                    fill={`url(#${uid}-horn)`}
                    stroke="#8fa1bd"
                    strokeWidth="1"
                />
                <circle cx={CX} cy={CY} r="17" fill={`url(#${uid}-horn)`} stroke="#8fa1bd" strokeWidth="1" />
                {[32, 46, 60, 74, 88].map((x, i) => (
                    <circle key={x} cx={CX + x} cy={CY} r={i === 4 ? 1.8 : 2.4} fill={i % 2 === 0 ? "#c0392b" : "#5b6b82"} />
                ))}
                <circle cx={CX} cy={CY} r="7" fill="#9aa8bd" stroke="#6b7a92" strokeWidth="1" />
                <line x1={CX - 5} y1={CY} x2={CX + 5} y2={CY} stroke="#4a5a72" strokeWidth="1.8" strokeLinecap="round" />
            </g>
        </svg>
    );
}