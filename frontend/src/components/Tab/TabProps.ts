import { ReactNode } from "react";

export default interface Props {
    title: string;
    eventKey:  any | undefined;
    children?: React.ReactNode;
    disabled?: boolean;
    // ...other props
}