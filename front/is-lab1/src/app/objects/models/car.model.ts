export interface Car {

    id: number;
    /**
     * Поле не может быть null
     */
    name: string;
  
    /**
     * Поле не может быть null
     */
    cool: boolean;

    isUser: string;

    adminsCanEdit: boolean;

    track: number;

    type: string;
  }