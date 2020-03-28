import { Component, OnInit } from '@angular/core';
import { Plant } from '../plant';
import { PLANTS } from '../mock-plants';

@Component({
    selector: 'garden',
    templateUrl: './garden.component.html',
    styleUrls: ['./garden.component.css']
})
export class GardenComponent implements OnInit {
    plants = PLANTS;
    selectedPlant: Plant;

    constructor() { }

    ngOnInit() {
    }

    onSelect(plant: Plant): void {
        this.selectedPlant = plant;
    }
}
