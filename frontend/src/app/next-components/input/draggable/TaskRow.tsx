"use client";

import { PureComponent } from "react";
import { ListGroup, Badge, Button } from "react-bootstrap";
import type { SortableBinding } from "./SortableBridge";
import { ObjectRecord } from "../../layout/Content";

type TaskRowProps = {
  task: ObjectRecord;
  index: number;
  binding: SortableBinding;
  locked: boolean;
};

export default class TaskRow extends PureComponent<TaskRowProps> {
  render() {
    const { task, index, binding, locked } = this.props;
    const { isDragging } = binding;

    return (
      <ListGroup.Item
        as="li"
        ref={binding.setNodeRef}
        className="d-flex align-items-center gap-3"
        active={isDragging}
        style={{
          ...binding.style,
          position: "relative",
          zIndex: isDragging ? 1 : "auto",
          boxShadow: isDragging ? "0 0.5rem 1rem rgba(0,0,0,.15)" : undefined,
        }}
      >
        {!locked && (
          <Button
            ref={binding.setActivatorNodeRef}
            {...binding.attributes}
            {...binding.listeners}
            variant="link"
            size="sm"
            className="p-0 text-secondary text-decoration-none"
            style={{ cursor: isDragging ? "grabbing" : "grab", touchAction: "none" }}
            aria-label={`Reorder ${task?.name}`}
          >
            ⠿
          </Button>
        )}

        <Badge bg="secondary" pill>
          {index + 1}
        </Badge>

        <div className="flex-grow-1">
          <div className="fw-semibold">{task?.name}</div>
          <small className={isDragging ? "" : "text-muted"}>{task?.time}</small>
        </div>
      </ListGroup.Item>
    );
  }
}