export interface EventComment {
    id: number;
    userId: number;
    eventId: number;
    parentId: number;
    comment: string;
    time: string;
    isPrivate: boolean;
}
