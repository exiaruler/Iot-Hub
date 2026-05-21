'use client'

import { RegularButton } from "@/app/next-components/buttons/RegularButton";
import SaveButton from "@/app/next-components/buttons/SaveButton";
import Form from "@/app/next-components/form/Form";
import ModeSubForm from "@/app/next-components/form/ModeSubForm";
import NewButton from "@/app/next-components/form/NewButton";
import SubForm from "@/app/next-components/form/SubForm";
import TextInput from "@/app/next-components/input/TextInput";
import { ContentRef, ObjectRecord } from "@/app/next-components/layout/Content";
import DeleteBox from "@/components/modal/DeleteBox";
import TabComponent from "@/components/Tab/TabComponent";
import React, { createRef, ReactNode, Ref, RefObject } from "react";
import { Col, Row } from "react-bootstrap";

interface ModeTabProps {
    // Layout records
    newModeLayout: ObjectRecord;
    boardActLayout: ObjectRecord;
    // Existing record to load (null when creating new)
    modeRecord?: ObjectRecord | null;
    // Index of this tab
    tabIndex: number;
    // Refs
    mainFormRef: RefObject<Form | null>;
    // Children: the compArr built in the parent
    children?: ReactNode;
    // Internal sub-form ref — created here but exposed so parent can push to formsCompRef
    modeSubFormRef: RefObject<Form | null>;
    boardActRef: RefObject<Form | null>;
    // board info for DeleteBox
    boardId: string;
    baseUrl: string;
    deleteModalRef: RefObject<any>;
    // Callbacks
    onTabBack: () => void;
    onAddMode: () => void;
    onRemoveMode: (index: number) => void;
    onModeClear: (ref: RefObject<SubForm | null>) => void;
    onModeNameChange?: (e: React.ChangeEvent<HTMLSelectElement>, index: number) => void;
    canDelete: boolean;
    tabTitle: string;
}

export default function ModeTab({
    newModeLayout,
    boardActLayout,
    modeRecord,
    tabIndex,
    mainFormRef,
    children,
    modeSubFormRef,
    boardActRef,
    boardId,
    baseUrl,
    deleteModalRef,
    onTabBack,
    onAddMode,
    onRemoveMode,
    onModeClear,
    onModeNameChange,
    canDelete,
    tabTitle,
}: ModeTabProps) {
    return (
        <TabComponent key={tabIndex} title={tabTitle || newModeLayout?.mode} eventKey={tabIndex}>
            <ModeSubForm
                recordLayout={newModeLayout || {}}
                idKey="id"
                record={modeRecord}
                ref={modeSubFormRef}
                objectKey="mode"
                index={tabIndex}
                formRef={mainFormRef}
                array={true}
            >
                <Row>
                    <Col md={4} xs={8}>
                        <TextInput
                            formRef={modeSubFormRef}
                            label="Mode"
                            required={true}
                            rows={0}
                            onChange={(e: React.ChangeEvent<HTMLSelectElement>) =>
                                onModeNameChange?.(e, tabIndex)
                            }
                            name="mode"
                        />
                        <SubForm
                            id="BoardAct-Form"
                            record={modeRecord?.boardAction}
                            recordLayout={boardActLayout || {}}
                            idKey="id"
                            ref={boardActRef}
                            objectKey="boardAction"
                            formRef={modeSubFormRef}
                        >
                            {children}
                        </SubForm>
                    </Col>
                </Row>
            </ModeSubForm>

            <Row>
                <Col xs={16}>
                    <RegularButton
                        caption="Back"
                        type="button"
                        onClick={onTabBack}
                        size={undefined}
                    />
                    <SaveButton caption="Save Function" size={undefined} />
                    <RegularButton
                        caption="Delete Mode"
                        type="button"
                        disabled={!canDelete}
                        onClick={() => onRemoveMode(tabIndex)}
                        size={undefined}
                    />
                    <DeleteBox
                        ref={deleteModalRef}
                        title="Delete"
                        deleteApi="/board/"
                        baseUrl={baseUrl}
                        param={boardId}
                    >
                        <p>Are you sure you want to delete this function?</p>
                    </DeleteBox>
                    <RegularButton
                        caption="Add Mode"
                        type="button"
                        onClick={onAddMode}
                        size={undefined}
                    />
                    <NewButton
                        onClick={() => onModeClear(boardActRef)}
                        formRef={modeSubFormRef}
                        caption="Clear Mode"
                        size={undefined}
                    />
                    <NewButton formRef={mainFormRef} caption="Clear" size={undefined} />
                </Col>
            </Row>
        </TabComponent>
    );
}