export interface ModalInterface{
    title:string,
    children?:any,
    buttonChildren?:any,
    submitCaption?:string,
    hideSubmit?:boolean,
    submit?:any,
    onClose?:any,
}
export interface ModalState{
    show:boolean,
    showSubmit:boolean,
    caption:string
}