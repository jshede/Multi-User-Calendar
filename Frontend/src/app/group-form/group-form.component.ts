import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { GroupService } from '../backend/services';
import { Group } from '../backend/objects';

@Component({
    selector: 'app-group-form',
    templateUrl: './group-form.component.html',
})
export class GroupFormComponent implements OnInit {
    failed = false;

    form = this.fb.group({
        name:        ['', Validators.required],
        description: ['', Validators.compose([Validators.required, Validators.maxLength(72)])],
    });

    constructor(
        private fb: FormBuilder,
        private server: GroupService,
    ) {}

    ngOnInit() {}

    createGroup() {
        // Check form validation
        if (this.form.invalid) {
            this.failed = true;
            return;
        }

        // Extract form values
        let group: Group = this.form.value;

        // Send event to server
        this.server.createGroup(group).subscribe(resp => { if (resp.ok) {
            console.log("Successfully created group");
        }});

        // Reset form
        this.form.reset();
        this.failed = false;
    }
}
