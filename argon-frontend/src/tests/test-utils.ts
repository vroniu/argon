import { ComponentFixture } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

export class TestUtils {
  static inputValueToField(fixture: ComponentFixture<any>, inputFieldId: string, inputFieldValue: string | number): void {
    let inputEl: HTMLInputElement = fixture.debugElement.query(By.css(`#${inputFieldId}`)).nativeElement;
    inputEl.focus();
    inputEl.value = inputFieldValue as string;
    inputEl.dispatchEvent(new Event('input'));
    inputEl.blur();
  }

  static inputValuesToFields(fixture: ComponentFixture<any>, idsAndValues: {id: string, value: string | number}[]): void {
    idsAndValues.forEach(({id, value}) => {
      this.inputValueToField(fixture, id, value);
    });
  }

  static clickButton(fixture: ComponentFixture<any>, buttonId: string) {
    let buttonEl: HTMLButtonElement = fixture.debugElement.query(By.css(`#${buttonId}`)).nativeElement;
    buttonEl.click();
  }
}
