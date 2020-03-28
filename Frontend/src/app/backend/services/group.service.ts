import { Injectable } from '@angular/core';
import { ServerService } from './server.service';
import { User, Group } from '../objects';

@Injectable({
    providedIn: 'root'
})
export class GroupService {
    constructor(private server: ServerService) {}

    createGroup(group: Group) {
        return this.server.post('createGroup', group);
    }

    deleteGroup(groupId: number) {
        return this.server.post('deleteGroup', { groupId });
    }

    editGroup(group: Group) {
        return this.server.post('editGroup', group);
    }

    getGroupInfo(groupId: number) {
        return this.server.get<Group>('getGroupInfo', { groupId });
    }

    getGroups() {
        return this.server.get<Group[]>('getGroups');
    }

    getUsersInGroup(groupId: number) {
        return this.server.get<User[]>('getUsersInGroup', { groupId });
    }
    
    inviteResponse(groupId: number, email: string){
        const inviteData = {groupId, email};
        return this.server.post('inviteResponse', inviteData );
    }

    kickUser(groupId: number, userId: number){
        const kickData = { groupId, userId };
        return this.server.post('#getKicked>:o', kickData);
    }

}
