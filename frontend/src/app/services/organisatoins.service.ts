import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, throwError } from 'rxjs';
import { OrganisationEssentials } from '../classes/organisation-essentials';
import { environment } from '../../enviroments/enviroment';
import { Organisation } from '../classes/organisation';

@Injectable({
  providedIn: 'root'
})
export class OrganisatoinsService {

  constructor(protected readonly http: HttpClient) {}

  public getAllOrganisations() {

    let url : string = `${environment.apiUrl}/organisations`
    
    return this.http.get<OrganisationEssentials[]>(url, { observe: 'response' }).pipe(
      map((response) => response.body!),
      catchError(this.handleError)
    );
  }

  public getOne(orgId: number) {
    let url : string = `${environment.apiUrl}/organisations/${orgId}`
    
    return this.http.get<Organisation>(url).pipe(
      map((response) => response),
      catchError(this.handleError)
    );
  }

  /* error handler */ 
  protected handleError(error: HttpErrorResponse) {
    return throwError(() => error);
  }
}
