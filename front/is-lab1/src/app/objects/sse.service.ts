import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SseService {

  constructor(private zone: NgZone) {}

  private eventSource?: EventSource;

  getServerSentEvent(url: string): Observable<any> {
    return new Observable(observer => {
      this.eventSource = new EventSource(url, { withCredentials: true });

      this.eventSource.addEventListener('data', (event: MessageEvent) => {
        this.zone.run(() => {
          observer.next(JSON.parse(event.data));
        });
      });

      this.eventSource.onerror = error => {
        this.zone.run(() => {
          observer.error(error);
        });
        this.eventSource?.close(); // Закрываем поток при ошибке
      };

      return () => {
        this.eventSource?.close(); // Закрываем соединение при завершении Observable
        console.log("SSE connection closed.");
      };
    });
  }

  closeConnection(): void {
    if (this.eventSource) {
      this.eventSource.close(); // Явно закрываем поток
      this.eventSource = undefined;
      console.log("SSE connection manually closed.");
    }
  }
  
}