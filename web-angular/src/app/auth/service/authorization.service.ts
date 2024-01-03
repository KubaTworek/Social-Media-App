import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {BehaviorSubject, Observable} from "rxjs";
import {UserData} from "../shared/user-data-type";

@Injectable({
  providedIn: 'root',
})
export class AuthorizationService {
  private loginErrorSubject = new BehaviorSubject<string>('');
  private registerErrorSubject = new BehaviorSubject<string>('');
  private userSubject = new BehaviorSubject<UserData | null>(this.getUserData());
  private isLoggedInSubject = new BehaviorSubject<boolean>(this.isLoggedIn());
  isLoggedIn$ = this.isLoggedInSubject.asObservable();

  constructor(private router: Router) {
  }

  getLoginError(): Observable<string> {
    return this.loginErrorSubject.asObservable();
  }

  getRegisterError(): Observable<string> {
    return this.registerErrorSubject.asObservable();
  }

  handleLogin(userData: UserData) {
    if (userData) {
      this.storeUserData(userData);
      this.userSubject.next(userData);
      this.isLoggedInSubject.next(true);
      this.router.navigate(['/home']);
    }
  }

  handleRefresh(userData: UserData) {
    if (userData) {
      this.storeUserData(userData);
      this.userSubject.next(userData);
    }
  }

  getUser(): Observable<UserData | null> {
    return this.userSubject.asObservable();
  }

  handleRegister() {
    this.router.navigate(['auth/login']);
  }

  logout() {
    this.removeUserData();
    this.isLoggedInSubject.next(false);
    this.router.navigate(['auth/login']);
  }

  handleLoginError(errorMessage: string) {
    this.loginErrorSubject.next(errorMessage);
  }

  handleRegisterError(errorMessage: string) {
    this.registerErrorSubject.next(errorMessage);
  }

  getToken(): string | null {
    return this.getUserData()?.token || null;
  }

  getRefreshToken(): string | null {
    return this.getUserData()?.refreshToken || null;
  }

  getUsername(): string | null {
    return this.getUserData()?.username || null;
  }

  getRole(): string | null {
    return this.getUserData()?.role || null;
  }

  followAuthor() {
    const userData = this.getUserData();
    if (userData) {
      userData.following = String(Number(userData.following) + 1)
      this.storeUserData(userData);
      this.userSubject.next(userData);
    }
  }

  unfollowAuthor() {
    const userData = this.getUserData();
    if (userData) {
      userData.following = String(Number(userData.following) - 1)
      this.storeUserData(userData);
      this.userSubject.next(userData);
    }
  }

  isSessionExpired(): boolean {
    const userData = this.getUserData();
    const logoutTime = userData ? Number(userData.tokenExpirationDate) : null;
    return logoutTime !== null && logoutTime <= new Date().getTime();
  }

  isRefreshTokenExpired(): boolean {
    const userData = this.getUserData();
    const logoutTime = userData ? Number(userData.refreshTokenExpirationDate) : null;
    return logoutTime !== null && logoutTime <= new Date().getTime();
  }

  private isLoggedIn(): boolean {
    return sessionStorage.getItem('userData') !== null;
  }

  private getUserData(): UserData | null {
    const userDataJson = sessionStorage.getItem("userData");
    return userDataJson ? JSON.parse(userDataJson) : null;
  }

  private storeUserData(userData: UserData) {
    sessionStorage.setItem('userData', JSON.stringify(userData));
  }

  private removeUserData() {
    sessionStorage.removeItem('userData');
  }
}
