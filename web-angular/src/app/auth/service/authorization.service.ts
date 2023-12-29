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

  constructor(
    private router: Router,
  ) {
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
    this.router.navigate(['auth/login']);
  }

  handleLoginError(errorMessage: string) {
    this.loginErrorSubject.next(errorMessage);
  }

  handleRegisterError(errorMessage: string) {
    this.registerErrorSubject.next(errorMessage);
  }

  getToken(): string | null {
    const userDataJson = this.getUserData();
    return userDataJson ? String(userDataJson.token) : null;
  }

  getRefreshToken(): string {
    const userDataJson = this.getUserData();
    return String(userDataJson.refreshToken);
  }

  getUsername(): string | null {
    const userDataJson = this.getUserData();
    return userDataJson ? String(userDataJson.username) : null;
  }

  getRole(): string | null {
    const userDataJson = this.getUserData();
    return userDataJson ? String(userDataJson.role) : null;
  }

  followAuthor() {
    const userData = this.getUserData();
    userData.following = String(Number(userData.following) + 1)
    this.storeUserData(userData)
    this.userSubject.next(userData);
  }

  unfollowAuthor() {
    const userData = this.getUserData();
    userData.following = String(Number(userData.following) - 1)
    this.storeUserData(userData)
    this.userSubject.next(userData);
  }

  private getUserData(): UserData {
    const userDataJson = sessionStorage.getItem("userData");
    return userDataJson ? JSON.parse(userDataJson) : null;
  }

  private storeUserData(userData: UserData) {
    console.log(userData)
    sessionStorage.setItem('userData', JSON.stringify(userData));
  }

  private removeUserData() {
    sessionStorage.removeItem('userData');
  }

  isSessionExpired() {
    const userDataJson = this.getUserData();
    const logoutTime = userDataJson ? Number(userDataJson.tokenExpirationDate) : null;

    return logoutTime !== null && logoutTime <= new Date().getTime();
  }

  isRefreshTokenExpired() {
    const userDataJson = this.getUserData();
    const logoutTime = userDataJson ? Number(userDataJson.refreshTokenExpirationDate) : null;

    return logoutTime !== null && logoutTime <= new Date().getTime();
  }
}
