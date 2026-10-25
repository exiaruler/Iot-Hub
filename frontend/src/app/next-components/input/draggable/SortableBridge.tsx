"use client";

import React from "react";
import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";

export type SortableBinding = {
  setNodeRef: (el: HTMLElement | null) => void;
  setActivatorNodeRef: (el: HTMLElement | null) => void;
  attributes: Record<string, any>;
  listeners: Record<string, any> | undefined;
  style: React.CSSProperties;
  isDragging: boolean;
};

type SortableBridgeProps = {
  id: string;
  disabled?: boolean;
  children: (binding: SortableBinding) => React.ReactNode;
};

/**
 * useSortable is a hook, and hooks can't be called inside class components.
 * This is the only function component in the feature: it calls the hook and
 * hands the result to a class through a render prop.
 */
export default function SortableBridge({ id, disabled, children }: SortableBridgeProps) {
  const {
    attributes,
    listeners,
    setNodeRef,
    setActivatorNodeRef,
    transform,
    transition,
    isDragging,
  } = useSortable({ id, disabled });

  return (
    <>
      {children({
        setNodeRef,
        setActivatorNodeRef,
        attributes: attributes as Record<string, any>,
        listeners,
        isDragging,
        style: { transform: CSS.Transform.toString(transform), transition },
      })}
    </>
  );
}