import { Component } from '@angular/core';
import { ObjectsService } from '../objects.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, ParamMap, RouterLink } from '@angular/router';
import { EntityType } from '../objects-manager/objects-manager.component';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-import',
  standalone: true,
  imports: [RouterLink, NgIf],
  templateUrl: './import.component.html',
  styleUrl: './import.component.scss'
})
export class ImportComponent {

  entityType: EntityType | null = null;
  
  selectedFile: File | null = null;
  jsonObject: String | null = null;

  result: string = '';

  importHistory: any[] | null = null;

  constructor(private objectsService: ObjectsService, private route: ActivatedRoute) {
    this.route.paramMap.subscribe((params: ParamMap) => {
      const entityTypeParam = params.get('entityType');
      this.entityType = entityTypeParam as EntityType;
      objectsService.getImportHistory(this.entityType).subscribe({
        next: (result) => {
          this.importHistory = result;
          console.log(result);
        }
      })
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  onUpload() {
    if (this.entityType == null || this.selectedFile == null) return;

    const reader = new FileReader();
    reader.onload = (e: ProgressEvent<FileReader>) => {
      try {

        this.jsonObject = JSON.parse(e.target?.result as string);
        this.objectsService.importObjects(this.entityType, JSON.stringify(this.jsonObject, null, 2)).subscribe({
          next: (response) => {
            this.result = "Imported objects.";
          },
          error: (error) => {
            this.result = error.error.message;
          }
        });

      } catch (error) {
        console.error(error);
      }
    };
    reader.readAsText(this.selectedFile);
  }
}
