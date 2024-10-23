import { Coordinates } from './coordinates.model';
import { Car } from './car.model';
import { Mood } from './mood.enum';
import { WeaponType } from './weapon-type.enum';

export interface HumanBeing {
  /**
   * Значение поля должно быть больше 0, уникально и генерируется автоматически
   */
  id: number;

  /**
   * Поле не может быть null, строка не может быть пустой
   */
  name: string;

  /**
   * Поле не может быть null
   */
  coordinates: string;

  /**
   * Поле не может быть null, генерируется автоматически
   */
  creationDate: string; // Можно использовать тип Date, если удобно

  /**
   * Поле не может быть null
   */
  realHero: boolean;

  /**
   * Поле не может быть null
   */
  hasToothpick: boolean;

  /**
   * Поле может быть null
   */
  car?: string;

  /**
   * Поле не может быть null
   */
  mood: Mood;

  /**
   * Поле не может быть null
   */
  impactSpeed: number;

  /**
   * Поле не может быть null
   */
  soundtrackName: string;

  /**
   * Поле не может быть null
   */
  weaponType: WeaponType;

  isUser: string;

  track: number;

  type: string;
}