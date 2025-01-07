import { Album } from "./album";

export interface Organisation {
    id: number;
    name: string;
    description: string;
    albums: Album[];
}
