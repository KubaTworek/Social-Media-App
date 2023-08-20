import {Component, OnDestroy} from '@angular/core';
import {NgForm} from "@angular/forms";
import {DataStorageService} from "../../shared/data-storage.service";
import {RegisterRequest} from "../dto/register-request.type";
import {Subscription} from "rxjs";
import {AuthorizationService} from "../service/authorization.service";

@Component({
  selector: 'register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnDestroy {

  errorMessage: string = '';
  registerErrorSubscription: Subscription;

  constructor(
    private authorizationService: AuthorizationService,
    private dataStorageService: DataStorageService,
  ) {
    this.registerErrorSubscription = this.authorizationService.getRegisterError().subscribe(error => {
      this.errorMessage = error;
    });
  }

  ngOnDestroy(): void {
    this.registerErrorSubscription.unsubscribe();
  }

  onSubmit(form: NgForm): void {
    if (!form.valid) {
      return;
    }

    const email = form.value.username;
    const password = form.value.password;
    const firstName = form.value['first-name'];
    const lastName = form.value['last-name'];

    const registerRequest: RegisterRequest = {
      username: email,
      password: password,
      firstName: firstName,
      lastName: lastName,
      role: 'ROLE_USER',
    };

    this.dataStorageService.register(registerRequest);

    form.reset();
  }
}
