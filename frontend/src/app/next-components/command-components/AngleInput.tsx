'use client'

import React from "react";
import { Row, Col, Form } from "react-bootstrap";
import TextInput from "../input/TextInput";
import Warning from "@/components/formGenComponents/Warning";
import { RecordContext } from "@/component-base/form/FormHandle";
import ServoDial from "../hardware/Servodial";
import ModalButton from "../modal/modal-button/modal-button";

// AngleInput-specific props on top of whatever TextInput already declares.
// NOTE: I don't have TextInput's prop types, so this widens them loosely -
// swap `any` for TextInput's real Props type if it exports one.
type AngleInputProps = any & {
    min?: number;   // default 0
    max?: number;   // default 180
    step?: number;  // default 1
};

export default class AngleInput extends TextInput {
    static contextType = RecordContext;
    public value = 0;
    public type = 'number';

    declare props: AngleInputProps;

    private get min() { return this.props.min ?? 0; }
    private get max() { return this.props.max ?? 180; }
    private get step() { return this.props.step ?? 1; }

    // Routes through TextInput's own onChange so validation / context-write /
    // Warning plumbing all keep working exactly as it does for typed input.
    // ASSUMPTION: TextInput.onChange reads event.target.name / event.target.value.
    // If TextInput's onChange signature differs, swap this call for whatever
    // it actually expects.
    private handleDialChange = (angle: number) => {
        this.onChange({
            target: { name: this.props.name, value: String(angle) },
        } as unknown as React.ChangeEvent<HTMLInputElement>);
    };

    render() {
        const angle = Number(this.getStateValue()) || 0;
        const disabled = !!(this.props.disable || this.props.readOnly);

        return (
            <Row>
                <Col xs={this.props.xs} md={this.props.md}>
                    <Form.Group hidden={this.props.hidden}>
                        <Form.Label>{this.props.label}</Form.Label>
                        <div className="mb-3 d-flex flex-column align-items-start gap-2">
                            <ModalButton buttonCaption={"Dial"} title={"Servo Dial"}>
                                <ServoDial
                                value={angle}
                                min={this.min}
                                max={this.max}
                                step={this.step}
                                disabled={disabled}
                                label={this.props.label}
                                onChange={this.handleDialChange}
                                />
                            </ModalButton>
                          
                            <Form.Control
                                readOnly={this.props.readOnly}
                                disabled={this.props.disable}
                                style={{ width: this.props.size }}
                                id={this.props.name + "Text"}
                                required={this.props.required}
                                onChange={(event: React.ChangeEvent<HTMLInputElement>) => this.onChange(event)}
                                name={this.props.name}
                                type={'number'}
                                min={this.min}
                                max={this.max}
                                step={this.step}
                                value={this.getStateValue()}
                            />
                        </div>
                        <Warning name={this.name} warning={this.getWarning()} ref={this.warningComponent} />
                    </Form.Group>
                </Col>
            </Row>
        );
    }
}