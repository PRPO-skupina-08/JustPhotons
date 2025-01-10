import { Inject, Injectable } from '@angular/core';
import { BehaviorSubject, catchError, map, Observable, throwError } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../enviroments/enviroment';
import { HttpClient } from '@angular/common/http';

export interface RegisterRequest {
  username: string,
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface JwtJson {
  token: string;
}

export interface JwtPayload {
  id: number;
  username: string;
  iat: number;
  exp: number;
}

const TOKEN_KEY = 'access_jwt_f7bcd2a86';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  constructor(protected readonly http: HttpClient) {}

  private tokenSubject = new BehaviorSubject<string | null>(this.getToken());

  login(credentials: LoginRequest) {
    let url : string = `${environment.apiUrl}/login`

    return this.http.post<JwtJson>(url, credentials).pipe(
      map((response) => {
        const token = response.token;
        this.setToken(token);
        return token;
      }),
      catchError((error) => {
        console.error('Error occurred:', error);
        return throwError(() => new Error('Neuspešna prijava.'));
      })
    );
  }

  register(userData: RegisterRequest): Observable<string> {
    let url : string = `${environment.apiUrl}/register`

    return this.http.post<string>(url, userData).pipe(
      map((response) => {
        const token = response;
        this.setToken(token);
        return token;
      })
    );
  }

  private setToken(token: string) {
    localStorage.setItem(TOKEN_KEY, token);
    this.tokenSubject.next(token);
  }

  public get currentToken(): string | null {
    return this.tokenSubject.value;
  }

  private getToken() {
    return localStorage.getItem(TOKEN_KEY);
  }

  public isLoggedIn() {
    const token = this.currentToken;
    return !!token && !this.isTokenExpired(token);
  }

  private isTokenExpired(token: string) {
    try {
      const decoded = jwtDecode<JwtPayload>(token);
      return decoded.exp < Date.now() / 1000;
    } catch {
      return true;
    }
  }

  public getUserInfo(): JwtPayload | null {
    const token = this.currentToken;

    if (!token) {
      return null;
    }

    try {
      return jwtDecode<JwtPayload>(token);
    } catch {
      return null;
    }
  }

  public logout() {
    localStorage.removeItem(TOKEN_KEY);
    this.tokenSubject.next(null);
  }
}
