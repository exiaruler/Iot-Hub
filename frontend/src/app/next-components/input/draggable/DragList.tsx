"use client";

import { ListGroup, Form } from "react-bootstrap";
import {
  DndContext,
  closestCenter,
  type CollisionDetection,
  type DragEndEvent,
} from "@dnd-kit/core";
import {
  arrayMove,
  SortableContext,
  verticalListSortingStrategy,
} from "@dnd-kit/sortable";
import { RecordContext } from "@/component-base/form/FormHandle";
import InputBase, { InputInterface as Base } from "@/component-base/input/InputBase";
import TaskRow from "./TaskRow";
import SortableBridge from "./SortableBridge";
import { ObjectArray, ObjectRecord } from "../../layout/Content";
import SubForm, { PropsMod } from "../../form/SubForm";
import { createRef } from "react";
// npm install react-bootstrap bootstrap @dnd-kit/core @dnd-kit/sortable @dnd-kit/utilities @dnd-kit/modifiers

/**
 * The input's value is the ordered Task[].
 *
 * Standalone:  <SortableListInput label="Order" rows={0} value={tasks} onChange={setTasks} />
 * In a form:   <SortableListInput label="Order" rows={0} name="tasks" formRef={formRef} />
 */
interface InputInterface extends Base,PropsMod {

}
export default class DragList extends InputBase {
  // InputBase declares `static context`, which React ignores. contextType is
  // what makes this.context (FormContext) available to InputBase.
  declare props:InputInterface;
  static contextType = RecordContext;
  private sortableIds = new WeakMap<object, string>();
  private nextSortableId = 0;
  private listRef = createRef<HTMLUListElement>();

  private collisionDetection: CollisionDetection = (args) => {
    const listBounds = this.listRef.current?.getBoundingClientRect();
    const pointer = args.pointerCoordinates;

    if (
      listBounds &&
      pointer &&
      (pointer.x < listBounds.left ||
        pointer.x > listBounds.right ||
        pointer.y < listBounds.top ||
        pointer.y > listBounds.bottom)
    ) {
      return [];
    }

    return closestCenter(args);
  };

  constructor(props:InputInterface) {
          super(props);
          this.warningComponent=createRef();
          this.state = {
              value:[],
              warning:""
          };
  }
  public value=this.props.value||[];
  // Current ordered tasks, from the form record when bound, else local state
  private getTasks(): ObjectArray {
    const raw = this.getStateValue();
    return Array.isArray(raw) ? raw : [];
  }
  public addToList(record:ObjectRecord):void{
    if(this.props.formRef){
        const form=this.props.formRef.current;
        let arr=this.getStateValue();
        arr=[...arr,record];
        form.onChangeRecord(this.props.name,arr);
      }
  }
  private getSortableIds(tasks: ObjectArray): string[] {
    const occurrences = new Map<object, number>();

    return tasks.map((task, index) => {
      if (task === null) return `task-null-${index}`;

      let id = this.sortableIds.get(task);
      if (!id) {
        id = `task-${++this.nextSortableId}`;
        this.sortableIds.set(task, id);
      }

      const occurrence = occurrences.get(task) ?? 0;
      occurrences.set(task, occurrence + 1);
      return `${id}-${occurrence}`;
    });
  }

  private handleDragEnd = ({ active, over }: DragEndEvent) => {
    const tasks = this.getTasks();
    const sortableIds = this.getSortableIds(tasks);
    const oldIndex = sortableIds.indexOf(String(active.id));
    if (oldIndex < 0) return;

    if (!over) {
      this.onChangeValue(tasks.filter((_, index) => index !== oldIndex));
      return;
    }
    if (active.id === over.id) return;
    const newIndex = sortableIds.indexOf(String(over.id));
    if (newIndex < 0) return;
    this.onChangeValue(arrayMove(tasks, oldIndex, newIndex));
  };

  render() {
    if (this.props.hidden) return null;

    const tasks = this.getTasks();
    const sortableIds = this.getSortableIds(tasks);
    const warning = this.getWarning();
    const locked = !!(this.props.readOnly || this.props.disable);

    return (
      <Form.Group className="mb-3">
        <Form.Label>{this.props.label}</Form.Label>

        <DndContext
          collisionDetection={this.collisionDetection}
          onDragEnd={this.handleDragEnd}
        >
          <SortableContext items={sortableIds} strategy={verticalListSortingStrategy}>
            <ListGroup as="ul" className="border rounded" ref={this.listRef}>
              {tasks.map((task:ObjectRecord, key:number) => (
                <SortableBridge key={sortableIds[key]} id={sortableIds[key]} disabled={locked}>
                  {(binding) => (
                    <SubForm recordLayout={this.props.recordLayout} idKey={this.props.idKey} array={true} index={key}>
                    <TaskRow task={task} index={key} binding={binding} locked={locked} />

                    </SubForm>
                    
                    
                  )}
                </SortableBridge>
              ))}
            </ListGroup>
          </SortableContext>
        </DndContext>

        <div ref={this.warningComponent}>
          {warning && <Form.Text className="text-danger">{warning}</Form.Text>}
        </div>
      </Form.Group>
    );
  }
}