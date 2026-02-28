import { Card,CardBody,CardTitle,CardText, Col, Row, Button } from "react-bootstrap";
import PageGroup from "../next-components/pageGroup";
import Client from "./client";
import { RegularButton } from "../next-components/buttons/RegularButton";
import { NextBase } from "@/NextBase";

async function getBoards() {
  let dataResp={
    boards:[],
    hardwares:[]
  };
  const base=new NextBase();
  // fetch user boards
  let boards=await base.fetchClientGet('/board/board');
  dataResp.boards=boards;
  // fetch hardware boards
  dataResp.hardwares=await base.fetchClientGet('/hardware/hardware');
  return dataResp;
}
export default async function Page(){
    const data=await getBoards();
    return(
    <PageGroup>
    <Client boards={data.boards} hardwares={data.hardwares}/>
    </PageGroup>
    );
}