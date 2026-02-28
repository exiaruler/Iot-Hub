interface Props{
    html:string|'';
}
export default function HTMLRender(props:Props){
    return(
        <div dangerouslySetInnerHTML={{__html:props.html}}/>
    );
}