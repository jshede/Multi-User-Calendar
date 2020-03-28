export interface Event {
    id: number;
    creatorId: number;
    groupId: number;
    description: string;
    start: string;
    end: string;
    isHighPriority: boolean;
    location: string;
    name: string;
}
