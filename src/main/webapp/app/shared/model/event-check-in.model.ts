import { Moment } from 'moment';

export interface IEventCheckIn {
  id?: number;
  userName?: string;
  address?: string;
  checkTime?: Moment;
  isCheckIn?: boolean;
  phoneNumber?: string;
}

export class EventCheckIn implements IEventCheckIn {
  constructor(
    public id?: number,
    public userName?: string,
    public address?: string,
    public checkTime?: Moment,
    public isCheckIn?: boolean,
    public phoneNumber?: string
  ) {
    this.isCheckIn = this.isCheckIn || false;
  }
}
