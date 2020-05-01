import { Email } from './email';
import { Phone } from './phone';

export interface Doctor {
    id?: number;
    firstName: string;
    lastName: string;
    speciality: string;
    email: Email;
    phone: Phone;
}
