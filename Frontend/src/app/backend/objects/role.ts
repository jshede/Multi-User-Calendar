export interface Role {
    id: number;
    groupId: number;
    name: string;
    // TODO: Does this work? TypeScript does not have char.
    permissions: string;
}
