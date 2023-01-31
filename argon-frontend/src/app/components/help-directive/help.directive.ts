import { HelpComponent } from './help.component';
import { Directive, TemplateRef, ViewContainerRef, OnInit, ViewRef, ComponentFactoryResolver, Input, ElementRef } from '@angular/core';
import * as helpEntries from '../../../assets/help.json';

export interface HelpEntry {
  title: string;
  content: string[];
}


export interface HelpDirectiveOptions {
  helpKey: string;
  offsetTop?: string;
  offsetRight?: string;
}

@Directive({
  selector: '[argHelp]'
})
export class HelpDirective {
  defaultOffset = '1rem';
  helpComponentElement: HTMLElement;
  parentComponentElement: HTMLElement;

  constructor(
    private templateRef: TemplateRef<any>, private viewContainer: ViewContainerRef, private componentFactoryResolver: ComponentFactoryResolver) { }

  @Input() set argHelp(options: HelpDirectiveOptions) {
    this.viewContainer.createEmbeddedView(this.templateRef);
    const helpComponent = this.viewContainer.createComponent(this.componentFactoryResolver.resolveComponentFactory(HelpComponent));
    helpComponent.instance.help = (helpEntries as any)[options.helpKey];
    this.helpComponentElement = helpComponent.location.nativeElement as HTMLElement;
    this.parentComponentElement = helpComponent.location.nativeElement.parentElement as HTMLElement;
    this.helpComponentElement.style.position = 'absolute';
    this.helpComponentElement.style.top = `-${options.offsetTop ? options.offsetTop : this.defaultOffset}`;
    this.helpComponentElement.style.right = `-${options.offsetRight ? options.offsetRight : this.defaultOffset}`;
    this.parentComponentElement.style.position = 'relative';
  }

}
