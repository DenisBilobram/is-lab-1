import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'trackFilter',
  pure: true,
  standalone: true
})
export class TrackFilterPipe implements PipeTransform {

  transform(objects: any[] | null | undefined, currentPage: number, pageSize: number): any[] {
    if (!objects) return [];

    return objects.filter(object => 
      currentPage * pageSize <= object.track && 
      object.track < (currentPage + 1) * pageSize
    );
  }

}

