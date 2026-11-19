import { NextBase } from "@/NextBase";
import Client from "./client";

async function getData(entity: string, id: string) {
    const base = new NextBase();
    return await base.fetchGetApi([
        { api: "/log/get-record-logs/" + entity + "/" + id, key: "logs", result: [] }
    ]);
}

export default async function Page({ params }: any) {
    const { entity, id } = await params;
    const data = await getData(entity, id);

    return <Client logs={data?.logs ?? []} />;
}