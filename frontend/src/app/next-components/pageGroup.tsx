'use client'
import { NextUIBase } from '@/NextUIBase';
import { usePathname } from 'next/navigation'
interface Props{
    url?:string;
    children?:any
}
export default function PageGroup(props:Props){
    const util=new NextUIBase();
    const pathname = usePathname()
    var url='';
    var exPage=null;
    if(props.url){
        url=props.url;
    }
    exPage=util.getPageUrl(url);
    var show=true;
    if(util.checkPageUrl(pathname)||exPage!=null){
        show=false;
    }
    return(
        <div className={'Content-Box'}>
        {!show?
            props.children
        :null}
        {show?
        <div >
        <h1>Page does not exist</h1>
        </div>
        :null}
        </div>
    )
}