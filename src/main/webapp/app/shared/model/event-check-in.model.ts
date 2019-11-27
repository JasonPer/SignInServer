import { Moment } from 'moment';

export interface IEventCheckIn {
  id?: number;
  userName?: string;
  phoneNumber?: number;
  address?: string;
  checkTime?: Moment;
  isCheckIn?: boolean;
}

export class EventCheckIn implements IEventCheckIn {
  constructor(
    public id?: number,
    public userName?: string,
    public phoneNumber?: number,
    public address?: string,
    public checkTime?: Moment,
    public isCheckIn?: boolean
  ) {
    this.isCheckIn = this.isCheckIn || false;
  }
}
