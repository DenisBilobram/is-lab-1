export interface Coordinates {

    id: number;
    /**
     * Значение поля должно быть больше -419
     */
    x: number;
  
    /**
     * Значение поля должно быть больше -453
     */
    y: number;

    isUser: string;

    adminsCanEdit: boolean;

    track: number;

    type: string;
  }